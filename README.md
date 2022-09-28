# The Ever-changing Hierarchy GmbH

http://localhost:8080/api/v1/register

**Body payload**
```json
{
    "name": "Hoang Vu",
    "email":"acv2@gmail.com",
    "password":"password"
}
```
**Body response: 200**
```
"1d39375d-a33e-4da7-aeb2-5aa077ff0303"
```

http://localhost:8080/api/v1/login

**Body payload**
```json
{
    "email":"acv2@gmail.com",
    "password":"password"
}
```
**Body response: 200**
```json
{
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY3YyQGdtYWlsLmNvbSIsImV4cCI6MTY2NDk0MjA0MCwiaWF0IjoxNjY0MzM3MjQwfQ.aEGNmW8LHJgKFNJuPp5v1ef5rLfOwxsuemaTQKXAToQ"
}
```

http://localhost:8080/api/v1/employees

**Headers**

```
Authorization
```
```
Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY3YyQGdtYWlsLmNvbSIsImV4cCI6MTY2NDUwNzkyMywiaWF0IjoxNjYzOTAzMTIzfQ.2dqF860-EixiHaBUppSKusEOiBvZ39WVn-qolNd6Y6I
```

**Body payload**
```json
{
    "Pete": "Nick",
    "Barbara": "Nick",
    "Nick": "Sophie",
    "backend": "Barbara",
    "frontend": "Pete",
    "Sophie": "Jonas"
}
```
**Body response: 200**
```json
{
    "Jonas": {
        "Sophie": {
            "Nick": {
                "Pete": {
                    "frontend": {}
                },
                "Barbara": {
                    "backend": {}
                }
            }
        }
    }
}
```

**Body payload**
```json
{
}
```
**Body response: 500**
```
"message": "Invalid Hierarchy",...
```

http://localhost:8080/api/v1/employees/backend

**Body response: 200**
```json
[
    "Barbara",
    "Nick"
]
```