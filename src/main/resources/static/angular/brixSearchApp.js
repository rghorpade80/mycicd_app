var searchApp = angular.module('brixSearchApp', ["webix", "ui.router"]);
var graphApp = angular.module('graphApp', ["webix", "ui.router"]);

//searchApp.config(function($locationProvider, $urlRouterProvider, $stateProvider, $httpProvider) {
//	
//    $stateProvider
//        .state('tableView', {
//            url: '/tableView',
//            templateUrl: '../brixsearchangular/views/tableview.html'
//           // template: 'brixsearchangular/views/tableview.html'
//            //controller:'BrixSerachTableController'
//        })
//        .state('treeView', {
//            url: '/treeView',
//            templateUrl: '../brixsearchangular/views/treeview.html'
//            //template: 'brixsearchangular/views/treeview.html'
//           // controller:'BrixSerachTreeController'
//        })
//        .state('treeView.project', {
//            url: '/project',
//            template: 'project'
//            //template: 'brixsearchangular/views/treeview.html'
//           // controller:'BrixSerachTreeController'
//        })
//        .state('treeView.testplan', {
//            url: '/testplan',
//            template: 'testplan'
//            //template: 'brixsearchangular/views/treeview.html'
//           // controller:'BrixSerachTreeController'
//        })
//        .state('treeView.test', {
//            url: '/test',
//            template: 'test'
//            //template: 'brixsearchangular/views/treeview.html'
//           // controller:'BrixSerachTreeController'
//        })
//});
//searchApp.config(['$locationProvider', function($locationProvider) {
//    $locationProvider.hashPrefix('');
//}]);