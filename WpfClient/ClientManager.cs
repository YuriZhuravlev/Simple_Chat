using System;
using System.Collections.Generic;
using System.Text;

namespace WpfClient
{
    class ClientManager
    {
        static Client client;
        public const string IP_ADDRESS= "192.168.0.103";
        public const int PORT = 8006;

        public static Client GetClient()
        {
            if (client == null)
            {
                client = new Client(IP_ADDRESS, PORT);
            }
            return client;
        }
    }
}
