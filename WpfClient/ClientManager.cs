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

        public static Client GetClient(string address = "")
        {
            if (client == null || address != "")
            {
                client = new Client(address, PORT);
            }
            return client;
        }
    }
}
