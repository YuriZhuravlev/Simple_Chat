using System;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Threading;

namespace WpfClient
{
    public class Client
    {
        protected internal NetworkStream Stream { get; private set; }
        public string userName { get; private set; }
        TcpClient client;

        public Client(string address, int port)
        {
            client = new TcpClient();
            client.Connect(address, port);
        }

        public bool SetUserName(string username)
        {
            userName = username;
            Stream = client.GetStream();
            byte[] data = Encoding.UTF8.GetBytes(userName);
            Stream.Write(data, 0, data.Length);
            return (client.Connected);
        }

        public void Send(string message)
        {
            byte[] data = Encoding.UTF8.GetBytes(message);
            Stream.Write(data, 0, data.Length);
        }

        public void Listen(ref ListBox listBox, Action<string> action)
        {
            try
            {
                string message;
                while (true)
                {
                    try
                    {
                        message = GetMessage();
                        if (message == null || message.Length == 0)
                        {
                            message = "Соединение потеряно";
                            action(message);
                            break;
                        }
                        action(message);
                        //wind.Dispatcher.Invoke(() => { listBox.Items.Add(message); });
                        
                    }
                    catch
                    {
                        message = "Соединение потеряно";
                        action(message);
                        //listBox.Items.Add(message);
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                action(e.Message);
                //listBox.Items.Add(e.Message);
            }
            finally
            {
                Close();
            }
        }

        // чтение входящего сообщения и преобразование в строку
        private string GetMessage()
        {
            byte[] data = new byte[64]; // буфер для получаемых данных
            StringBuilder builder = new StringBuilder();
            int bytes = 0;
            do
            {
                bytes = Stream.Read(data, 0, data.Length);
                builder.Append(Encoding.UTF8.GetString(data, 0, bytes));
            }
            while (Stream.DataAvailable);

            return builder.ToString();
        }

        protected internal void Close()
        {
            if (Stream != null)
                Stream.Close();
            if (client != null)
                client.Close();
        }
    }
}