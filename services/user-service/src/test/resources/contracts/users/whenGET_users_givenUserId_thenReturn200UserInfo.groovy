package contracts.users

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return 200 and user data when get user by id"
  request {
    method GET()
    url($(regex('/users/[0-9]{1,8}')))
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
            firstName: 'Hen',
            lastName : 'Tran Sam',
            userName : 'tshen',
            email    : 'tshen.petproject@tshen.com'
        ]
    )
  }
}
