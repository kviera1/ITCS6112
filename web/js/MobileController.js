/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app = angular.module("app", []);
app.controller("mobileController", function mobileController($scope) {
    $scope.mobiles = [{name: "Galaxy", price:199},
                      {name: "Iphone", price:299},
                      {name: "Moto", price:99}];
});

