using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace WpfClient
{
    /// <summary>
    /// Логика взаимодействия для Window1.xaml
    /// </summary>
    public partial class Auth : Window
    {
        public Auth()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (AuthTextBox.Text.Length == 0)
            {
                ErrorLabel.Content = "Пустое имя!";
                ErrorLabel.Visibility = Visibility.Visible;
            }
            else
            {
                int ev = ClientManager.GetClient().SetUserName(AuthTextBox.Text);
                switch (ev)
                {
                    case Client.EVENT_CONNECT:
                        this.Hide();
                        MainWindow mainWindow = new MainWindow();
                        mainWindow.Show();
                        break;
                    case Client.EVENT_ERROR_NAME:
                        ErrorLabel.Content = "Такое имя уже занято!";
                        ErrorLabel.Visibility = Visibility.Visible;
                        break;
                    case Client.EVENT_ERROR_CONNECT:
                        ErrorLabel.Content = "Ошибка подключения";
                        ErrorLabel.Visibility = Visibility.Visible;
                        break;
                };
            }
        }
    }
}
