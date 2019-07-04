**Avaliação para desenvolvedor Java - Banco Inter**

---
Avaliação de Elvis Fernandes para a vaga de desenvolvedor JAVA no Banco Inter
---
## Arquitetura do projeto

O projeto é dividido em classe de negocios, entidades, recursos e manipuladores de exceção.
Api - Aplicação RESTful

## Frameworks e bibliotecas empregadas no desenvolvimento

Spring framework e Java 11 é a base do projeto.
Base de dados H2 e JpaRepositories
JUnit para Testes unitários, juntamente com o mockito
Lombok para redução de código Boilerplate
Swagger UI para testar endpoints

---

## HighLights

O projeto foi desenvolvido de forma bem completa em sua base.
Conceitos de Herança de funcionalidade e funcionalidades genéricas foram aplicadas, tais como:
ModelMapper genérico, para converter entidades para DTOs e de volta
Tratador de Exceções personalizado com mensagens customizadas no message.properties com suporte a i18n.

---

## Como executar

Necessário ter o maven instalado na maquina para executar os comandos abaixo.

O projeto deve ser compilado em sua pasta raiz (mvn install).
Para iniciar os testes é necessário executar o comando mvn test
Para que a aplicação inicialize é necessário entrar no diretório application (cd application) e executar mvn spring-boot:run

Para testar os endpoints basta abrir a url abaixo no navegador assim que o projeto inicializar.
http://localhost:8080/api/swagger-ui.html


Desde já, agradeço a todos pela oportundiade
