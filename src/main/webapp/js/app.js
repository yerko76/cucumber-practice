(function () {

    var app = angular.module('awards', [
        'ngRoute',
        'awards.controllers',
        'awards.services'
    ]);

    app.config(['$routeProvider','$locationProvider',
        function ($routeProvider,$locationProvider) {

        $routeProvider
            .when('/categories', {
                templateUrl: 'views/category.html',
                controller: 'CategoryController'
            })
            .when('/nominees',{
                templateUrl: 'views/nominees.html',
                controller: 'NomineeController'
            })
            .when('/nominees/:category-id',{
              templateUrl: 'views/nominees.html',
                controller: 'NomineeController'
            });
    }]);

})();

