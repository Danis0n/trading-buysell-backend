# trading-buysell-backend
```
The simplified version of buy-sell application. Was developed as a study project.
```
Application supports simple `JWT` Authentication&Authorization and mail verification.

`GET, DELETE, POST` requests are supported for `all` of users. There are also several types of user's `roles`.

## Roles

`Super Admin` - The super role, that allows to manage all of the rest users (include `admin` role).

`Admin` - The role with same rights as `Super Admin`.

`Manager` - The role, that allows to moderate other users.

`User` - The most spreaded role. Issues after registration.

## Registration

To register a new `user`, you should pass `JSON file` or `form-data` through with POST request `/api/register` with next parametrs: 

| KEY | EXAMPLE |
| ------ | ------ |
| Name | Example Name  |
| Username | Example Username |
| Password | 1234 |
| Phone | +123456789 |
| Email | example@mail.com |

After registration is success, the mail with verification token is sent to new user.

## Login

To login as existing user, you should pass `JSON file` or form-data through with POST request `/api/login` with next parametrs:
| KEY | EXAMPLE |
| ------ | ------ |
| Username | Example Username |
| Password | 1234 |

After login is success, you should recieve a response of `two` `Json Web Tokens`:

- `JWT access` - the token that includes your authorities, username and expiration date. 
You may manually change the expiration date. By default, you have about 10 minutes.

- `JWT refresh` - the token that includes your username and expiration date. It purpose is to refresh the access token for futher work on authenticated account. In case of date is expired, you should to re-login.

## Dependencies

The project uses next dependencies for stable work:
- Java 11
- [Spring Framework](https://spring.io) (JPA, WEB, SECURITY, OAUTH2, MAIL) for as a core 
- [Lombok](https://projectlombok.org) for comfort
- [Postgres](https://www.postgresql.org) as a database
- [JWT](https://jwt.io) as a safe data transfer helper

## Installation

To install the project, just copy it to your directory `manually` or use `git` command `git copy`.

Makesure, you have all of dependencies marked `above` of latest versions.
