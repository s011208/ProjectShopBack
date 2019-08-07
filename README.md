[![CircleCI](https://circleci.com/gh/s011208/ProjectShopBack.svg?style=svg)](https://circleci.com/gh/s011208/ProjectShopBack)

# ProjectShopBack

## Libs
[MvRx] + [Epoxy] + [Dagger] + [RxJava] + [Retrofit]

## Features
Create an Android App which shows the GitHub users in a List and Detail View.

## References
List - https://developer.github.com/v3/users/#get-all-users

Pagination - https://developer.github.com/v3/#link-header

Detail https://developer.github.com/v3/users/#get-a-single-user

## Questions
### What was your assumptions and design considerations when doing the exercise

I choose [MvRx] + [Epoxy] as the main structure in this project. 
Like MVVM, [MvRx] can put most of the business logics in view-models. 
[Epoxy] helps us to modulize distinct items in recyclerview 
and re-use modules in distinct recyclers easier. 
Besides, by using dependency injections framework, [Dagger] in this project, 
it is easy to inject mock objects when running unit tests.

### What did you find most difficult
hmmm..

### Any other comments you would like to tell us



[MvRx]: https://github.com/airbnb/MvRx
[Epoxy]: https://github.com/airbnb/epoxy
[Dagger]: https://github.com/google/dagger
[RxJava]: https://github.com/ReactiveX/RxJava
[Retrofit]: https://github.com/square/retrofit
