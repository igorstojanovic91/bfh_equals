import router from './router.js';
import login from './components/login.js';
import logout from './components/logout.js';
import modules from './components/modules.js';
import courses from './components/courses.js';
import assessment from './components/assessment.js';
import notify from "./components/notify.js";

router.register('/', login);
router.register('/logout', logout);
router.register('/modules', modules);
router.register('/module-overview', courses);
router.register('/assessment', assessment);
router.register('/notify', notify);

router.go('/');
