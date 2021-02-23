import router from './router.js';
import login from './components/login.js';
import casOverview from './components/casoverview.js';


router.register('/', login);
router.register('/casoverview', casOverview);

router.go('/');
