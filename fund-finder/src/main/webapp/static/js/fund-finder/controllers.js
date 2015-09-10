angular
    .module('fundFinder')

    // ==============================================================================================================
    // 	Security Controller
    // ==============================================================================================================
    .controller('SecurityCtrl', function($rootScope, $scope, $http, $state, Administrator_TotalService) {
    	
    	/**
    	 * Login function.
    	 */
    	$scope.login = function() {
			$http.post('login', $.param($scope.credentials), {
				headers: {
					"content-type" : "application/x-www-form-urlencoded"
				}
			}).success(function(data) {
				authenticate(function() {
					if ($rootScope.role == 'ROLE_USER') {
						if ($rootScope.authenticated) {
							$state.go('user.company_edit', { 'mode' : 'show' });
						} else {
							$state.go('login');
						}
					} else {
						$state.go(($rootScope.authenticated) ? 'administrator.dashboard' : 'login');
					}
				});
			}).error(function(data, status) {
				$state.go('login');
				$rootScope.authenticated = false;
				toastr.error('Unijeli ste netočne korisničke podatke');
			})
		};
		
		/**
		 * Logout function.
		 */
		$scope.logout = function() {
			$http.post('logout', {}).success(function() {
				$rootScope.authenticated = false;
				$state.go('login');
			}).error(function(data) {
				$rootScope.authenticated = false;
				$state.go('login');
			});
		};
		
		/**
		 * Forgot password.
		 */
		$scope.forgotPassword = function() {
			$state.go('forgot_password');
		};
		
		/**
		 * Registration.
		 */
		$scope.registration = function() {
			$state.go('registration');
		};
		
		var authenticate = function(callback) {
			$http.get('/user').success(function(data) {
				if (data.username) {
					$rootScope.authenticated = true;
					$rootScope.username = data.username;
					$rootScope.fullName = data.firstName + " " + data.lastName;
					$rootScope.role = data.role;	
					
					callback && callback();
				} else {
					$rootScope.authenticated = false;
					callback && callback();
					$state.go('login');
				}
			}).error(function() {
				$rootScope.authenticated = false;
				callback && callback();
				$state.go('login');
			});
		}

		// initial load
		authenticate();
		$scope.credentials = { username: "superadmin@gmail.com", password: "admin" };
		Administrator_TotalService.updateTotal();
    })
    
    // ==============================================================================================================
    // 	Dashboard Controller
    // ==============================================================================================================
    .controller('DashboardCtrl', function($rootScope, $scope, $state) {
    	$scope.layout = 'grid';
    	$scope.users = [
    	    { fullName: "User Name 1", company: "Company Name 1", oib: "324234234332" },
    	    { fullName: "User Name 2", company: "Company Name 2", oib: "788979234789" },
    	    { fullName: "User Name 3", company: "Company Name 3", oib: "934276423739" },
    	    { fullName: "User Name 4", company: "Company Name 4", oib: "313488893443" },
    	    { fullName: "User Name 5", company: "Company Name 5", oib: "512923734673" },
    	];  
    	
    	$scope.showDetails = function(user) {

    	};
    })
    
    .controller('StatisticsOverviewCtrl', function($rootScope, $scope, $http, $state) {
    	
    })
    