using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Windows.Forms;

namespace DailyWirdDesktop
{
    static class Program
    {
        private static HttpListener _listener;
        private static Thread _serverThread;
        private static bool _isRunning = true;
        private static int _port = 49218;

        [STAThread]
        static void Main()
        {
            // Find an available port if default is busy
            _port = FindFreePort(_port);

            // Start internal HTTP server for 100% local offline rendering
            try
            {
                _listener = new HttpListener();
                _listener.Prefixes.Add("http://localhost:" + _port + "/");
                _listener.Prefixes.Add("http://127.0.0.1:" + _port + "/");
                _listener.Start();

                _serverThread = new Thread(ServerWorker);
                _serverThread.IsBackground = true;
                _serverThread.Start();
            }
            catch (Exception ex)
            {
                MessageBox.Show("تعذر بدء المخدم المحلي: " + ex.Message, "الورد اليومي", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }

            // Find Edge or Chrome executable
            string browserPath = FindBrowserPath();
            if (string.IsNullOrEmpty(browserPath))
            {
                MessageBox.Show("لم يتم العثور على محرك العرض (Microsoft Edge أو Chrome). يرجى التأكد من توفر المتصفح على جهازك.", "الورد اليومي", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            string dataDir = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "DailyWirdDesktop", "Profile");
            string appUrl = "http://localhost:" + _port + "/";
            string args = string.Format("--app=\"{0}\" --window-size=1360,880 --user-data-dir=\"{1}\" --no-first-run --no-default-browser-check", appUrl, dataDir);

            ProcessStartInfo psi = new ProcessStartInfo(browserPath, args);
            psi.UseShellExecute = false;

            try
            {
                Process proc = Process.Start(psi);
                if (proc != null)
                {
                    proc.WaitForExit();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("خطأ أثناء تشغيل النافذة: " + ex.Message, "الورد اليومي", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            finally
            {
                _isRunning = false;
                try { if (_listener != null && _listener.IsListening) _listener.Stop(); } catch { }
            }
        }

        private static void ServerWorker()
        {
            while (_isRunning && _listener != null && _listener.IsListening)
            {
                try
                {
                    HttpListenerContext context = _listener.GetContext();
                    ThreadPool.QueueUserWorkItem(ProcessRequest, context);
                }
                catch
                {
                    break;
                }
            }
        }

        private static void ProcessRequest(object state)
        {
            HttpListenerContext context = (HttpListenerContext)state;
            HttpListenerResponse resp = context.Response;

            try
            {
                string rawUrl = context.Request.RawUrl;
                if (rawUrl == "/" || rawUrl.StartsWith("/?"))
                {
                    rawUrl = "/index.html";
                }

                // First priority: check for local file in current directory
                string localPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, rawUrl.TrimStart('/').Replace('/', Path.DirectorySeparatorChar));
                byte[] buffer = null;
                string mimeType = "text/html; charset=utf-8";

                if (File.Exists(localPath))
                {
                    buffer = File.ReadAllBytes(localPath);
                    mimeType = GetMimeType(localPath);
                }
                else
                {
                    // Fallback to embedded resource
                    string resourceName = "DailyWird.index.html";
                    Assembly asm = Assembly.GetExecutingAssembly();
                    using (Stream stream = asm.GetManifestResourceStream(resourceName))
                    {
                        if (stream != null)
                        {
                            using (MemoryStream ms = new MemoryStream())
                            {
                                stream.CopyTo(ms);
                                buffer = ms.ToArray();
                            }
                        }
                    }
                }

                if (buffer != null)
                {
                    resp.StatusCode = 200;
                    resp.ContentType = mimeType;
                    resp.ContentLength64 = buffer.Length;
                    resp.Headers.Add("Cache-Control", "no-cache");
                    resp.OutputStream.Write(buffer, 0, buffer.Length);
                }
                else
                {
                    resp.StatusCode = 404;
                    byte[] notFound = Encoding.UTF8.GetBytes("Not Found");
                    resp.OutputStream.Write(notFound, 0, notFound.Length);
                }
            }
            catch
            {
            }
            finally
            {
                try { resp.Close(); } catch { }
            }
        }

        private static string GetMimeType(string path)
        {
            string ext = Path.GetExtension(path).ToLowerInvariant();
            switch (ext)
            {
                case ".html": case ".htm": return "text/html; charset=utf-8";
                case ".css": return "text/css; charset=utf-8";
                case ".js": return "application/javascript; charset=utf-8";
                case ".json": return "application/json; charset=utf-8";
                case ".png": return "image/png";
                case ".jpg": case ".jpeg": return "image/jpeg";
                case ".ico": return "image/x-icon";
                case ".svg": return "image/svg+xml";
                case ".mp3": return "audio/mpeg";
                default: return "application/octet-stream";
            }
        }

        private static int FindFreePort(int startPort)
        {
            for (int p = startPort; p < startPort + 100; p++)
            {
                try
                {
                    System.Net.Sockets.TcpListener l = new System.Net.Sockets.TcpListener(IPAddress.Loopback, p);
                    l.Start();
                    l.Stop();
                    return p;
                }
                catch
                {
                }
            }
            return startPort;
        }

        private static string FindBrowserPath()
        {
            string[] candidates = new string[]
            {
                Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ProgramFilesX86), @"Microsoft\Edge\Application\msedge.exe"),
                Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ProgramFiles), @"Microsoft\Edge\Application\msedge.exe"),
                Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), @"Microsoft\Edge\Application\msedge.exe"),
                Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ProgramFiles), @"Google\Chrome\Application\chrome.exe"),
                Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ProgramFilesX86), @"Google\Chrome\Application\chrome.exe")
            };

            foreach (string p in candidates)
            {
                if (File.Exists(p)) return p;
            }
            return null;
        }
    }
}
