import { useState } from 'react';
import { Field, subscriptionCategories } from '../Common.jsx';

export default function SubscriptionForm({ initial, onSave, onCancel, busy }) {
  const [error, setError] = useState('');

  return (
    <form
      className="modal-form"
      onSubmit={async (event) => {
        event.preventDefault();
        const values = Object.fromEntries(new FormData(event.currentTarget));
        values.amount = Number(values.amount);
        const ok = await onSave(values);
        if (!ok) setError('Please check the values and try again.');
      }}
    >
      <Field label="Service name" name="name" defaultValue={initial.name || ''} placeholder="e.g. Music streaming" required />
      <Field label="Amount (₹)" name="amount" type="number" min="0.01" step="0.01" defaultValue={initial.amount || ''} placeholder="0.00" required />
      <label className="field"><span>Billing cycle</span><select name="billingCycle" defaultValue={initial.billingCycle || 'MONTHLY'}><option value="MONTHLY">Monthly</option><option value="YEARLY">Yearly</option></select></label>
      <label className="field"><span>Category</span><select name="category" defaultValue={initial.category || 'Entertainment'}>{subscriptionCategories.map((value) => <option key={value}>{value}</option>)}</select></label>
      <Field label="Next billing date" name="nextBillingDate" type="date" defaultValue={initial.nextBillingDate || new Date().toISOString().slice(0, 10)} required />
      {error && <p className="form-error">{error}</p>}
      <div className="modal-actions">
        <button type="button" className="button button-quiet" onClick={onCancel}>Cancel</button>
        <button className="button button-primary" disabled={busy}>{busy ? 'Saving…' : 'Save subscription'}</button>
      </div>
    </form>
  );
}
