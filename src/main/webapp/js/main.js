import router from './router.js';
import login from './components/login.js';
import modules from './components/modules.js';


router.register('/', login);
router.register('/modules', modules);

router.go('/');
