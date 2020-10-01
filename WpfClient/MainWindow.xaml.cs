using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace WpfClient
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        string[] Messages = new string[0];
        Thread mainThread = Thread.CurrentThread;
        Thread backgroundThread;

        public MainWindow()
        {
            InitializeComponent();

            backgroundThread = new Thread(Listener) { IsBackground = true };
            backgroundThread.Start();
            
        }

        private void Listener(object state)
        {
            Action<string> del;
            del = ChangeData;
            ClientManager.GetClient().Listen(ref MessageBox, del);
        }

        private void ChangeData(string message)
        {
            this.Dispatcher.Invoke(() => { MessageBox.Items.Add(message); });
        }

        private void Window_Closed(object sender, EventArgs e)
        {
            Application.Current.Shutdown();
        }

        private void MessageSenderButton_Click(object sender, RoutedEventArgs e)
        {
            if (MessageTextBox.Text.Length > 0)
            {
                ClientManager.GetClient().Send(MessageTextBox.Text);
                MessageTextBox.Text = "";
            }
        }
    }
}
