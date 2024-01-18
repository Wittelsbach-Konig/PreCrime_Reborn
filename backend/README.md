# PreCrime - backend

## Установка POSTGRE:
### Step 1
На Linux

```shell
$ sudo apt update
$ sudo apt install postgresql
```

### Step 2
После завершения установки вы можете убедиться, что служба PostgreSQL активна. Для чего в командной строке наберите:

```shell
$ sudo systemctl is-active postgresql
```
включена ли служба:

```shell
$ sudo systemctl is-enabled postgresql
```
статус:

```shell
$ sudo systemctl status postgresql
```

После чего, убедитесь, что PostgreSQL-сервер готов принимать подключения от клиентов:

```shell
$ sudo pg_isready
```

### Step 3
Создание базы данных в PostgreSQL:
```shell
$ sudo su - postgres
```
Подключившись, выполните команду psql:
```shell
$ psql
```
Создать БД:
```postgres
# CREATE DATABASE PreCrime;
```
Если вы видите приглашение ко вводу команд postgres=#, значит вы находитесь в оболочке СУБД PostgreSQL. 
И значит, можно приступать к созданию базы данных. 
Первая команда добавит в PostgreSQL пользователя bob (на своём сервере вы можете использовать свои имена пользователей и баз данных):
```postgres
# CREATE USER bob WITH PASSWORD 'P@$$w0rd';
```
Теперь, назначьте своему пользователю все права сразу на вашу базу данных. В нашем примере это будет выглядеть так:
```postgres
# GRANT ALL PRIVILEGES ON DATABASE bobdb to bob;
```
Чтобы покинуть оболочку, наберите:
```postgres
# \q
```
И теперь, можно отключить от системы пользователя postgres:
```shell
$ exit
```

## Запуск Backend

Введите следующие команды в терминал:

```bash
mvn spring-boot:run
```
Для упаковки с необходимым профилем:

```bash
mvn clean package -Pprod
```

## Selenium

Для авто тестов селениума необходимо установить chrome-driver. Для этой задачи есть специальный скрипт:
```bash
chmod +x install-chrome.sh
```
```bash
./install-chrome.sh
```