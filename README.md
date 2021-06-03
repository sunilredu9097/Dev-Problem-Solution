# Dev-Problem-Solution

Get Client API:
==============
URL:  http://localhost:8080/client/api/v1/

Case1: Without passing any request parameter it gives all clients

Output:
[
    {
        "firstName": "JP",
        "lastName": "Dumini",
        "mobileNumber": "8754565356",
        "idNumber": "7801014800084",
        "address": {
            "city": "Durban",
            "country": "South Africa",
            "streetLine1": "Line 1",
            "streetLine2": "Line 2"
        }
    }
]

Case1: get Client by firstname or mobileNumber of Idnumber  
        A. Find by mobile number
             http://localhost:8080/client/api/v1/?mobileNumber=8754565356
             
                 Output       : [
                              {
                                  "firstName": "JP",
                                  "lastName": "Dumini",
                                  "mobileNumber": "8754565356",
                                  "idNumber": "7801014800084",
                                  "address": {
                                      "city": "Durban",
                                      "country": "South Africa",
                                      "streetLine1": "Line 1",
                                      "streetLine2": "Line 2"
                                  }
                              }
                          ]
             
        Note: Mobile Number must be valid mobile number otherwise we will get below response
                       O/P :     {
                                      "timestamp": "2021-06-03T07:58:23.117+00:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "getClient.mobileNumber: Mobile Number must have 10 digits",
                                      "path": "/client/api/v1/",
                                      "subErrors": [
                                          "mobileNumber is Required"
                                      ]
                                  }
                                  
Create Client API:
=================                              
                    URL: http://localhost:8080/client/api/v1
                    Request Body: {
                                    "firstName": "steve",
                                    "lastName": "smith",
                                    "mobileNumber": "7042339097",
                                    "idNumber": "2303014800086",
                                    "address": {
                                        "city": "Johansberg",
                                        "country": "SA",
                                        "streetLine1": "Line 1",
                                        "streetLine2": "Line2"
                                    }
                                }
                                  
                   firstName,lastName and idNumber are mandatory.
                   IdNumber should be a valid SA Id number.if not,we will get below error
                   
                    URL: http://localhost:8080/client/api/v1
                    Request Body: {
                                    "firstName": "steve",
                                    "lastName": "smith",
                                    "mobileNumber": "7042339097",
                                    "idNumber": "2303014800086",
                                    "address": {
                                        "city": "Johansberg",
                                        "country": "SA",
                                        "streetLine1": "Line 1",
                                        "streetLine2": "Line2"
                                    }
                                }
                                
                         Reskponse:       
                                     {
                                        "timestamp": "2021-06-03T08:03:34.825+00:00",
                                        "status": 400,
                                        "error": "Bad Request",
                                        "message": "SA ID number is not valid: 2403014800086",
                                        "path": "/client/api/v1/",
                                        "subErrors": null
                                    }
if we try to create client with same idNumber or Mobile number then we get below reponse:
                           
                            {
                            "timestamp": "2021-06-03T08:09:24.717+00:00",
                            "status": 409,
                            "error": " Already Exist",
                            "message": "Client already exist with Id Number: 2303014800086",
                            "path": "/client/api/v1/",
                            "subErrors": null
                           }
                           
                           or 
                           {
                              "timestamp": "2021-06-03T08:10:44.480+00:00",
                              "status": 409,
                              "error": " Already Exist",
                              "message": "Client already exist with Mobile Number: 7042339097",
                              "path": "/client/api/v1/",
                              "subErrors": null
                          }
                                  
update Client API:(put)
======================                             
                    URL: http://localhost:8080/client/api/v1/{idNumber}
                    Request Body: {
                                    "firstName": "steve",
                                    "lastName": "smith",
                                    "mobileNumber": "7042339097",
                                    "idNumber": "2303014800086",
                                    "address": {
                                        "city": "Johansberg",
                                        "country": "SA",
                                        "streetLine1": "Line 1",
                                        "streetLine2": "Line2"
                                    }
                                }
                                  
                   idNumber pathvariable is mandatory.
                   
                   if recourd is not found for idNumber the we get below response otherwise record is updated.

                           {
                              "timestamp": "2021-06-03T08:06:12.910+00:00",
                              "status": 404,
                              "error": "Data Not Found",
                              "message": "Client does not exist for given id Number: 3801014800084",
                              "path": "/client/api/v1/3801014800084",
                              "subErrors": null
                          }

                                  
                                  
