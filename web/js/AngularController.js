/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var fetch = angular.module('myapp', []);

fetch.controller('userCtrl', ['$scope', '$http', function ($scope, $http) {
 $http({
  method: 'get',
  url: 'hello'
 }).then(function(response) {
  // Store response data
  console.log("Success -> " + response.data);
  $scope.msgFromServlet = angular.fromJson(response.data);
  console.log(angular.fromJson(response.data));
 });
}]);
