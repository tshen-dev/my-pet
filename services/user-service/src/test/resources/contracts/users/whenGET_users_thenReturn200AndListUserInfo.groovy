package contracts.users

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return 200 and list user data when get users"
  request {
    method GET()
    url("/users")
    headers {
      contentType(applicationJson())
    }
  }
  response {
    headers {
      contentType(applicationJson())
    }
    status 200
    body(
        message: "SUCCESS",
        data: [
            [
                firstName: 'Hen',
                lastName : 'Tran Sam',
                userName : 'tshen',
                email    : 'tshen.petproject@tshen.com'
            ],
            [
                firstName: 'Teo',
                lastName : 'Tony',
                userName : 'ttony',
                email    : 'ttony.petproject@tshen.com'
            ]
        ]
    )
  }
}
