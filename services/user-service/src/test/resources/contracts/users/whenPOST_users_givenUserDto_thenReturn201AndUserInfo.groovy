package contracts.users

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return 201 and user data when create user with valid DTO"
  request {
    method POST()
    url("/users")
    headers {
      contentType(applicationJson())
    }
    body(
        firstName: $(c(anyNonBlankString()), p('Hen')),
        lastName: $(c(anyNonBlankString()), p('Tran Sam')),
        userName: $(c(anyNonBlankString()), p('tshen')),
        password: $(c(anyNonBlankString()), p('tshen')),
        email: $(c(anyEmail()), p('tshen.petproject@gmail.com'))
    )
  }
  response {
    headers {
      contentType(applicationJson())
    }
    status 201
    body(
        message: "SUCCESS",
        data: [
          firstName: fromRequest().body('$.firstName'),
          lastName: fromRequest().body('$.lastName'),
          userName: fromRequest().body('$.userName'),
          email: fromRequest().body('$.email')
        ]
    )
  }
}