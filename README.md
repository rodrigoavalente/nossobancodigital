# Nosso Banco Digital
Aplicação simples feita com spring tentando simular algumas funcionalidades de um banco digital.

A aplicação utiliza thymeleaf como template engine para a formatação dos e-mails, utiliza de filas do rabbitmq para cadastro de contas e processamento de transferências.

# Simple SMTP Server
O servidor smtp utilizado foi feito em python segundo esse [link](https://stackoverflow.com/questions/2690965/a-simple-smtp-server-in-python).

# RabbitMQ
Foi utilizado docker compese para criar um container para o rabbitmq com as seguintes informações:

```
rabbitmq:
  image: rabbitmq:management
  ports:
    - "5672:5672"
    - "15672:15672"
```



