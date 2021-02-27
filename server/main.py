import select
import socket
import datetime

IP_ADDRESS = '192.168.0.103'
PORT = 8006

# Фунция для отправки сообщений всем кроме отправителя
def send_to_all(sender, message):
    for connect_client in connected_list:
        if connect_client != server_socket and connect_client != sender:
            try:
                connect_client.send(message.encode('utf-8'))
            except:
                # если подключение разорвано
                connect_client.close()
                connected_list.remove(connect_client)


if __name__ == "__main__":
    name = ""
    # словарь для хранения адреса, соответствующего имени пользователя
    record = {}
    # Список для отслеживания дескрипторов сокетов
    connected_list = []
    buffer = 4096

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    server_socket.bind((IP_ADDRESS, PORT))
    server_socket.listen(10)  # listen atmost 10 connection at one time

    # Добавить серверный сокет в список доступных для чтения соединений
    connected_list.append(server_socket)

    print("SERVER WORKING")

    while 1:
        # Получить список сокетов, готовых к чтению
        rList, wList, error_sockets = select.select(connected_list, [], [])

        for sock in rList:
            # Подключение нового клиента
            if sock == server_socket:
                # Обработка случая, когда через server_socket получено новое соединение
                sockfd, addr = server_socket.accept()
                name = sockfd.recv(buffer).decode('utf-8')
                connected_list.append(sockfd)
                record[addr] = ""
                # print "record and conn list ",record,connected_list

                # если повторяется имя пользователя
                if name in record.values():
                    sockfd.send("Username already taken!\n".encode('utf-8'))
                    del record[addr]
                    connected_list.remove(sockfd)
                    sockfd.close()
                    continue
                else:
                    # sockfd.send("OK".encode('utf-8'))
                    # добавить имя и адрес
                    record[addr] = name
                    print(datetime.datetime.now().strftime("%d-%m-%Y %H:%M"), "|", "Client (%s, %s) connected" % addr, " [", record[addr], "]")

                    sockfd.send("Welcome to chat room. Enter 'tata' anytime to exit\r\n".encode('utf-8'))
                    send_to_all(sockfd, name + " joined the conversation\n")

            # Входящее сообщение от клиента
            else:
                # Данные от клиента
                try:
                    data1 = sock.recv(buffer)
                    # print "sock is: ",sock
                    data = data1.decode('utf-8')
                    # data = data1[:data1.index("\n")]
                    # print "\ndata received: ",data

                    # получить addr клиента, отправившего сообщение
                    i, p = sock.getpeername()
                    if data == "tata":
                        msg = record[(i, p)] + " left the conversation\n"
                        send_to_all(sock, msg)
                        print(datetime.datetime.now().strftime("%d-%m-%Y %H:%M"), "|", "Client (%s, %s) is offline" % (i, p), " [", record[(i, p)], "]")

                        del record[(i, p)]
                        connected_list.remove(sock)
                        sock.close()
                        continue

                    else:
                        sock.send((data + '\n').encode('utf-8'))
                        msg = record[(i, p)] + ": " + data  + '\n'
                        send_to_all(sock, msg)

                # внезапный выход пользователя
                except:
                    (i, p) = sock.getpeername()
                    send_to_all(sock,
                                record[(i, p)] + " left the conversation unexpectedly\n")
                    print(datetime.datetime.now().strftime("%d-%m-%Y %H:%M"), "|", "Client (%s, %s) is offline (error)" % (i, p), " [", record[(i, p)], "]\n")

                    del record[(i, p)]
                    connected_list.remove(sock)
                    sock.close()
                    continue

    server_socket.close()
