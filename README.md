# Система управления банковскими картами

### Для запуска необходимо: 
* установить Docker
* убедиться, что порты 8080 и 5430 свободны, или поменять на другие в application.yaml и docker-compose.yml
* выполнить команду <b>docker-compose up</b> 
* запустить через IDE главный класс BankcardsApplication

### Для проверки работы нужно:
* открыть в Postman  http://localhost:8080/auth/login
*  добавить тело запроса в формате JSON:<br>
  {<br>
  "login": "admin",<br>
  "password": "superadmin"<br>
  }
* Отправить POST-запрос
* Получить токен
* Добавлять во все запросы заголовок Authorization со значением пришедшего типа и токена через пробел, например:<br>
  Bearer eyJhbGci... ...oxNzc

Данные для авторизации как администратор:<br>
login: admin<br>
password: superadmin<br>

Данные для авторизации как пользователь:<br>
(для пользователя с ID=1)<br>
login: sdfoijvova<br>
password: 78щшлщщ89789<br>


Документация доступна по адресу http://localhost:8080/swagger-ui/index.html при запущенном приложении