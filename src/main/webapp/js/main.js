import router from './router.js';
import login from './components/login.js';
import modules from './components/modules.js';
import courses from './components/courses.js';

router.register('/', login);
router.register('/modules', modules);
router.register('/module-overview', courses);

router.go('/');
