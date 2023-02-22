package contracts.users

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return 200 when delete user with valid id"
  request {
    method DELETE()
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
        message: "SUCCESS"
    )
  }
}
