import { useEffect } from 'react';

export const money = (value) => new Intl.NumberFormat('en-IN', {
  style: 'currency', currency: 'INR', maximumFractionDigits: 0,
}).format(value || 0);

export const dateLabel = (value) => value ? new Date(`${value}T00:00:00`).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' }) : '—';

export const expenseCategories = ['FOOD', 'TRAVEL', 'SHOPPING', 'EDUCATION', 'ENTERTAINMENT', 'BILLS', 'HEALTH', 'OTHER'];
export const paymentMethods = ['CASH', 'UPI', 'CARD', 'BANK_TRANSFER'];
export const subscriptionCategories = ['Entertainment', 'Productivity', 'Utilities', 'Health', 'Other'];

export function Field({ label, ...props }) {
  return <label className="field"><span>{label}</span><input {...props} /></label>;
}

export function Modal({ title, onClose, children }) {
  useEffect(() => {
    const onKey = (event) => event.key === 'Escape' && onClose();
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [onClose]);

  return (
    <div className="modal-backdrop" onMouseDown={(event) => event.target === event.currentTarget && onClose()}>
      <section className="modal" role="dialog" aria-modal="true" aria-label={title}>
        <div className="modal-heading">
          <div>
            <span className="eyebrow">LEDGERLY</span>
            <h3>{title}</h3>
          </div>
          <button className="modal-close" onClick={onClose} aria-label="Close">×</button>
        </div>
        {children}
      </section>
    </div>
  );
}

export function EmptyState({ title, copy }) {
  return <div className="empty-state"><span className="empty-mark">—</span><strong>{title}</strong><p>{copy}</p></div>;
}

export function ErrorState({ message }) {
  return <div className="error-state"><strong>Couldn't load this view</strong><p>{message}</p></div>;
}

export function Loading() {
  return <div className="loading-state"><span />Loading your overview…</div>;
}

export function pretty(value = '') {
  return value.toLowerCase().replaceAll('_', ' ').replace(/\b\w/g, (letter) => letter.toUpperCase());
}
