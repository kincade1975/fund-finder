angular.module('fundFinder')

	.service('Administrator_TenderService', function($http) {
		this.getPage = function(resource) {
			return $http.post('/api/v1/tender/page', resource);
		};
		this.getTender = function(id) {
			return $http.get('/api/v1/tender/' + id);
		};
		this.saveTender = function(resource) {
			return $http.post('/api/v1/tender', resource);
		};
		this.deleteTender = function(id) {
			return $http.delete('/api/v1/tender/' + id);
		};
		this.activateTender = function(id) {
			return $http.get('/api/v1/tender/activate/' + id);
		};
		this.deactivateTender = function(id) {
			return $http.get('/api/v1/tender/deactivate/' + id);
		};
	});