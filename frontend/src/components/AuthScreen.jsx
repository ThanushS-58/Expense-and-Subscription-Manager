import { useState } from 'react';
import { send } from '../api.js';
import { Field } from './Common.jsx';

export default function AuthScreen({ onSignedIn }) {
  const [mode, setMode] = useState('login');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function submit(event) {
    event.preventDefault();
    const form = Object.fromEntries(new FormData(event.currentTarget));
    setLoading(true);
    setError('');

    try {
      const auth = await send(`/auth/${mode}`, 'POST', form);
      onSignedIn(auth);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-layout">
      <section className="auth-aside">
        <a className="brand auth-brand" href="#"><span className="brand-mark">l</span><span>ledgerly<span className="brand-period">.</span></span></a>
        <div className="auth-intro">
          <span className="eyebrow">A CLEARER VIEW OF YOUR MONEY</span>
          <h1>Spend with<br /><em>intention.</em></h1>
          <p>One calm place for everyday spending and the subscriptions that add up.</p>
        </div>
        <div className="auth-stamp"><span>01</span><div><strong>Your finances, in focus</strong><small>Private by design. Yours to manage.</small></div></div>
        <div className="auth-decoration" aria-hidden="true"><span /><span /><span /><span /><span /><span /><span /><span /><span /></div>
      </section>

      <section className="auth-panel">
        <div className="auth-form-wrap">
          <span className="eyebrow">{mode === 'login' ? 'WELCOME BACK' : 'GET STARTED'}</span>
          <h2>{mode === 'login' ? 'Sign in to Ledgerly' : 'Create your account'}</h2>
          <p className="form-subtitle">{mode === 'login' ? 'Your spending overview is waiting.' : 'A better handle on your money starts here.'}</p>

          <form onSubmit={submit} className="form-stack">
            {mode === 'register' && <Field label="Your name" name="name" autoComplete="name" placeholder="e.g. Aanya Shah" required />}
            <Field label="Email address" name="email" type="email" autoComplete="email" placeholder="you@example.com" required />
            <Field label="Password" name="password" type="password" autoComplete={mode === 'login' ? 'current-password' : 'new-password'} placeholder="At least 8 characters" required minLength="8" />
            {error && <p className="form-error">{error}</p>}
            <button className="button button-primary auth-submit" disabled={loading}>{loading ? 'Please wait…' : mode === 'login' ? 'Sign in' : 'Create account'}<span>→</span></button>
          </form>

          <p className="auth-switch">{mode === 'login' ? 'New to Ledgerly?' : 'Already have an account?'} <button onClick={() => { setMode(mode === 'login' ? 'register' : 'login'); setError(''); }}>{mode === 'login' ? 'Create an account' : 'Sign in'}</button></p>
          <div className="auth-privacy">⌁ <span>Your data is private and accessible only to you.</span></div>
        </div>
      </section>
    </div>
  );
}
