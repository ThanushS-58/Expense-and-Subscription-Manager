import { useEffect, useState } from 'react';
import { send } from './api.js';
import AuthScreen from './components/AuthScreen';
import Dashboard from './components/Dashboard';
import Expenses from './components/Expenses';
import Subscriptions from './components/Subscriptions';

function App() {
  const [token, setToken] = useState(() => localStorage.getItem('ledgerly-token'));
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem('ledgerly-user') || 'null'));
  const [page, setPage] = useState('Dashboard');
  const [refresh, setRefresh] = useState(0);
  const [notice, setNotice] = useState('');
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (!notice) return undefined;
    const timeout = setTimeout(() => setNotice(''), 3500);
    return () => clearTimeout(timeout);
  }, [notice]);

  function signedIn(auth) {
    localStorage.setItem('ledgerly-token', auth.token);
    localStorage.setItem('ledgerly-user', JSON.stringify(auth.user));
    setToken(auth.token);
    setUser(auth.user);
    setPage('Dashboard');
  }

  function signOut() {
    send('/auth/logout', 'POST').catch(() => {});
    localStorage.removeItem('ledgerly-token');
    localStorage.removeItem('ledgerly-user');
    setToken(null);
    setUser(null);
  }

  async function mutate(action, success) {
    setBusy(true);
    try {
      await action();
      setRefresh((count) => count + 1);
      setNotice(success);
      return true;
    } catch (error) {
      setNotice(error.message);
      return false;
    } finally {
      setBusy(false);
    }
  }

  if (!token) return <AuthScreen onSignedIn={signedIn} />;

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <a className="brand" href="#dashboard" onClick={() => setPage('Dashboard')}>
          <span className="brand-mark">l</span><span>ledgerly<span className="brand-period">.</span></span>
        </a>
        <div className="nav-label">WORKSPACE</div>
        <nav className="primary-nav" aria-label="Main navigation">
          {[['Dashboard', '◫'], ['Expenses', '↗'], ['Subscriptions', '⟳']].map(([name, icon]) => (
            <button key={name} className={`nav-item ${page === name ? 'active' : ''}`} onClick={() => setPage(name)}>
              <span className="nav-icon">{icon}</span>{name}
              {name === 'Dashboard' && <span className="nav-dot" />}
            </button>
          ))}
        </nav>

        <div className="sidebar-bottom">
          <div className="sidebar-rule" />
          <div className="profile-row">
            <div className="avatar">{user?.name?.slice(0, 1).toUpperCase() || 'U'}</div>
            <div className="profile-copy">
              <strong>{user?.name || 'Your account'}</strong>
              <span>{user?.email}</span>
            </div>
            <button className="signout-button" title="Sign out" onClick={signOut}>↗</button>
          </div>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div><span className="eyebrow">PERSONAL FINANCE</span><h1>{page}</h1></div>
          <div className="topbar-meta"><span className="live-dot" /> Everything in one place</div>
        </header>

        {notice && <div className="toast" role="status">{notice}<button onClick={() => setNotice('')} aria-label="Dismiss">×</button></div>}

        {page === 'Dashboard' && <Dashboard key={refresh} />}
        {page === 'Expenses' && <Expenses key={`expenses-${refresh}`} mutate={mutate} busy={busy} />}
        {page === 'Subscriptions' && <Subscriptions key={`subscriptions-${refresh}`} mutate={mutate} busy={busy} />}
      </main>
    </div>
  );
}

export default App;