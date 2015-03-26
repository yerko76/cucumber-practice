(function () {

    angular.module('awards.services', [])
        awardsApp.factory('categoryService', ['$http', '$q', function ($http, $q) {
            function all() {
                var deferred = $q.defer();

                $http.get('/awards/api/categories')
                    .success(function (data) {
                        deferred.resolve(data);
                    });

                return deferred.promise;
            }

            return {
                all: all
            };

        }]);

        awardsApp.factory('nomineeService', ['$http', '$q', function ($http, $q) {
            function getNomineesByCategoryId(categoryId){
                var deferred = $q.defer();

                $http.get('api/nominees/search?category-id=' + categoryId)
                    .success(function(data){
                        deferred.resolve(data);
                    });

                return deferred.promise;
            }

            return {
                getNomineesByCategoryId: getNomineesByCategoryId
            }

        }]);

        awardsApp.factory('voteService', ['$http', '$q', function ($http, $q) {
            function castVote(vote){
                console.log(vote);
                $http.post('api/vote',vote)
                    .success(function(data, status){
                        con casole.log(data);
                        console.log(status);
                    });
            }

            return {
                castVote: castVote
            }

        }]);
})();