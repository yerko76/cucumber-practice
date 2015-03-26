(function () {
    angular.module('awards.controllers', [])
        awardsApp.controller('CategoryController', ['$scope','categoryService', function ($scope, categoryService) {
            categoryService.all().then(function(data){
                $scope.categories = data;
            })

        }]);

        awardsApp.controller('NomineeController', [' $scope','nomineeService', function ($scope, nomineeService) {
            $scope.nominees = [];
            $scope.getNomineesByCategory = function(categoryId){
                nomineeService.getNomineesByCategoryId(categoryId).then(function(data){
                    $scope.nominees = data;
                })
            };
        }]);

        awardsApp.controller('VoteController', ['$scope','voteService', function ($scope, voteService) {
            $scope.vote = {};

            $scope.castVote = function(categoryId, nomineeId){
                $scope.vote.userId = "user id";
                $scope.vote.categoryId = categoryId;
                $scope.vote.nomineeId = nomineeId;

                voteService.castVote($scope.vote);
            };
        }]);

        awardsApp.controller('tabController', ['$scope', function($scope){
            $scope.tab = -1;
            $scope.selectedTab = function(id){
                $scope.tab = id;
            };
        }]);

})();
