import { useState } from 'react';
import { Field, paymentMethods, pretty } from '../Common.jsx';
import { expenseCategories } from '../Common.jsx';

export default function ExpenseForm({ initial, onSave, onCancel, busy }) {
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
      <Field label="Amount (₹)" name="amount" type="number" step="0.01" min="0.01" defaultValue={initial.amount || ''} placeholder="0.00" required />
      <label className="field"><span>Category</span><select name="category" defaultValue={initial.category || 'FOOD'} required>{expenseCategories.map((value) => <option key={value}>{value}</option>)}</select></label>
      <Field label="Description" name="description" defaultValue={initial.description || ''} placeholder="What was this for?" />
      <label className="field"><span>Payment method</span><select name="paymentMethod" defaultValue={initial.paymentMethod || 'UPI'} required>{paymentMethods.map((value) => <option key={value} value={value}>{pretty(value)}</option>)}</select></label>
      <Field label="Expense date" name="expenseDate" type="date" defaultValue={initial.expenseDate || new Date().toISOString().slice(0, 10)} required />
      {error && <p className="form-error">{error}</p>}
      <div className="modal-actions">
        <button type="button" className="button button-quiet" onClick={onCancel}>Cancel</button>
        <button className="button button-primary" disabled={busy}>{busy ? 'Saving…' : 'Save expense'}</button>
      </div>
    </form>
  );
}
