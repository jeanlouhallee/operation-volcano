#/bin/sh


curl --location --request POST 'http://localhost:8080/booking/v1/reservation' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName":"Homer",
    "lastName":"Simpsons",
    "email":"1@mail.com",
    "stayDates": {
        "fromDate":"2021-02-13",
        "untilDate":"2021-02-15"
    }
}' &

curl --location --request POST 'http://localhost:8080/booking/v1/reservation' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName":"Marge",
    "lastName":"Simpsons",
    "email":"2@mail.com",
    "stayDates": {
        "fromDate":"2021-02-14",
        "untilDate":"2021-02-16"
    }
}' &

curl --location --request POST 'http://localhost:8080/booking/v1/reservation' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName":"Bart",
    "lastName":"Simpsons",
    "email":"3@mail.com",
    "stayDates": {
        "fromDate":"2021-02-15",
        "untilDate":"2021-02-17"
    }
}' &

wait