# Shortcuts GiftCard API

A series of API endpoint can be used for activating a Shortcuts GiftCard, 
checking balances, redeem, etc.

## API Endpoints

### GiftCard Activation
This endpoint allows you to activate a Shortcuts GiftCard under an existing GiftCard program. 
It is worth mentioning that users will need to go to Shortcuts GiftCard Console to register a 
card number first before you activate a GiftCard.

Activation endpoint:
```http request, json
POST /giftcard/{giftcard_number}/activate
Host: api.shortcutssoftware.io
```

Example request:
```http request, json
https://api.shortcutssoftware.io/giftcard/62997400000000400401/activate

{
    "site_transaction_id":"1",
    "site_transaction_date_time":"2018-05-24T12:43:00",
    "activation_inc_tax_amount":200
}
```

example response:
```json
{
    "transaction_id": "1",
    "transaction_ex_tax_amount": 200,
    "transaction_inc_tax_amount": 200,
    "giftcard_ex_tax_balance": 200,
    "giftcard_inc_tax_balance": 200,
    "giftcard_expiry_date": "2019-05-30T00:00:00",
    "giftcard_currency_code": "AUS"
}
```

### Check Balance
This endpoint allows you to check the GiftCard balance. By simply providing a GiftCard number,
users will be able to retrieve GiftCard balance information as well as GiftCard program 
name.

Check balance endpoint: 
```http request, json
GET /giftcard/{giftcard_number}
Host: api.shortcutssoftware.io
```

Example request:
```http request
https://api.shortcutssoftware.io/giftcard/62997400000000400401
```

Example response : 
```json
{
    "transaction_id": "",
    "transaction_reference_id": "",
    "authorization_code": "",
    "transaction_ex_tax_amount": 0,
    "transaction_inc_tax_amount": 0,
    "card_holder_information": null,
    "member_balance": {
        "balance_ex_tax_amount": 200,
        "balance_inc_tax_amount": 200,
        "expiry_date": null,
        "currency_code": "AUS",
        "program_code": null,
        "program_name": "GiftCard",
        "member_number": "62997400000000400401",
        "pos_card_type_id": "GC",
        "balance_points": "0",
        "available_points": null,
        "card_type_code": "giftcard",
        "member_status_code": "none",
        "benefit_items": [],
        "payment_plans": []
    }
}
```

### Redeem
This API endpoint can be used to redeem a Shortcuts GiftCard. It will return 
the balance amount as well as transaction amount after a successful redemption.

Redeem endpoint:
```http request
POST /giftcard/{giftcard_number}/redeem
Host: api.shortcutssoftware.io
```

Example request:
```http request
https://api.shortcutssoftware.io/giftcard/62997400000000400401/redeem

{
    "site_transaction_id":"2",
    "site_transaction_date_time":"2018-05-24T12:45:00",
    "redemption_amount":25
}
```

Example response:
```json
{
    "transaction_id": "2",
    "transaction_reference_id": "",
    "authorization_code": "",
    "transaction_ex_tax_amount": -25,
    "transaction_inc_tax_amount": -25,
    "card_holder_information": {
        "is_email_unsubscribed": false
    },
    "member_balance": {
        "balance_ex_tax_amount": 175,
        "balance_inc_tax_amount": 175,
        "expiry_date": null,
        "currency_code": "AUS",
        "program_code": "",
        "program_name": "GiftCard",
        "member_number": "62997400000000400401",
        "pos_card_type_id": "GC",
        "balance_points": "0",
        "available_points": null,
        "card_type_code": "giftcard",
        "member_status_code": "none",
        "benefit_items": [],
        "payment_plans": []
    }
}
```

### Reload
This endpoint can be used to adjust the GiftCard balance. It will return the balance amount 
as well as the adjusting amount after a successful reload. 

Reload endpoint:
```http request
POST /giftcard/{giftcard_number}/reload
Host: api.shortcutssoftware.io
```

Example request:
```http request
https://api.shortcutssoftware.io/giftcard/62997400000000400401/reload

{
    "site_transaction_id":"3",
    "site_transaction_date_time":"2018-05-24T12:45:00",
    "reload_amount":25
}
```

Example response:
```json
{
    "transaction_id": "3",
    "transaction_reference_id": "",
    "transaction_ex_tax_amount": 25,
    "transaction_inc_tax_amount": 25,
    "giftcard_ex_tax_balance": 200,
    "giftcard_inc_tax_balance": 200,
    "giftcard_expiry_date": "2019-05-30T00:00:00",
    "giftcard_currency_code": "AUS"
}
```

### Cancel Last Operation
Cancel last operation can be used to cancel any redemption or reload operations. It requires the
transaction id and the exact amount from the last operation to invoke this endpoint. 

Cancel last operation endpoint:
```http request
POST /giftcard/{giftcard_number}/cancel_last_operation
Host: api.shortcutssoftware.io
```

Example request:
```http request
https://api.shortcutssoftware.io/giftcard/62997400000000400401/cancel_last_operation

{
    "site_transaction_id":"4",
    "site_transaction_date_time":"2018-05-24T12:46:00",
    "original_site_transaction_id":"3",
    "original_transaction_amount":25,
    "original_transaction_points":0
}
```

Example response:
```json
{
    "transaction_id": "4",
    "transaction_reference_id": "",
    "authorization_code": "",
    "transaction_ex_tax_amount": -25,
    "transaction_inc_tax_amount": -25,
    "card_holder_information": {
        "is_email_unsubscribed": false
    },
    "member_balance": {
        "balance_ex_tax_amount": 175,
        "balance_inc_tax_amount": 175,
        "expiry_date": null,
        "currency_code": "AUS",
        "program_code": null,
        "program_name": "GiftCard",
        "member_number": "62997400000000400401",
        "pos_card_type_id": "GC",
        "balance_points": "0",
        "available_points": null,
        "card_type_code": "giftcard",
        "member_status_code": "none",
        "benefit_items": [],
        "payment_plans": []
    }
}
```

##Example tests
1. Gain access to GiftCard program console. Register GiftCard numbers 
under Number Registration. (Number Registrations -> Register).
2. Go to ```test.properties``` file, change the value of ```site.serial_number``` 
3. In the same file, change the value of```giftcard.registered.ready```
with your registered GiftCard number.
