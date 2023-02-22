package contracts.users

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return 400 when create user with invalid DTO"
  request {
    method POST()
    url("/users")
    headers {
      contentType(applicationJson())
    }
    body(
        firstName: ''
    )
  }
  response {
    headers {
      contentType(applicationJson())
    }
    status 400
    body(
        message: "FirstName should not be empty"
    )
  }
}
