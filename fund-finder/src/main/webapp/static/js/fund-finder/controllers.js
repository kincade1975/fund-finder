angular
    .module('fundFinder')

    // ==============================================================================================================
    // 	Security Controller
    // ==============================================================================================================
    .controller('SecurityCtrl', function($rootScope, $scope, $http, $state) {
    	
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
		 * Edit current user data.
		 */
		$scope.editUser = function() {
			if ($rootScope.role == "ROLE_USER") {
				$state.go('user.user_edit', { 'id' : $rootScope.userId });
			} else {
				$state.go('administrator.user_edit', { 'id' : $rootScope.userId });
			}
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
					$rootScope.userId = data.id;
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
    })
    
    // ==============================================================================================================
    // 	Statistics Controller
    // ==============================================================================================================
    
    .controller('StatisticsOverviewCtrl', function($rootScope, $scope, $http, $state) {
    	
    })
    