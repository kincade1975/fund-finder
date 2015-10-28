angular.module('fundFinder')

	.service('Administrator_ImpressionService', function($http) {
		this.getImpressionStatistics = function(entity, entityId, statisticsPeriod) {
			return $http.get('/api/v1/impression/statistics/' + entity + "/" + entityId + "/" + statisticsPeriod);
		};
		this.getStatisticsPeriods = function() {
			return $http.get('/api/v1/impression/statistics/periods');
		};
	});