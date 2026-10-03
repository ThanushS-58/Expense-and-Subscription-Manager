import { useEffect, useState } from 'react';
import { send } from '../api.js';
import { ErrorState, Modal, money } from './Common.jsx';
import ExpenseFilters from './expenses/ExpenseFilters.jsx';
import ExpenseForm from './expenses/ExpenseForm.jsx';
import ExpenseHeader from './expenses/ExpenseHeader.jsx';
import ExpenseTable from './expenses/ExpenseTable.jsx';

export default function Expenses({ mutate, busy }) {
  const [items, setItems] = useState([]);
  const [filters, setFilters] = useState({ category: '', fromDate: '', toDate: '' });
  const [editing, setEditing] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const query = new URLSearchParams(Object.fromEntries(Object.entries(filters).filter(([, value]) => value)));
    send(`/expenses${query.size ? `?${query}` : ''}`, 'GET')
      .then(setItems)
      .catch((err) => setError(err.message));
  }, [filters]);

  async function save(values) {
    const ok = await mutate(
      () => send(editing?.id ? `/expenses/${editing.id}` : '/expenses', editing?.id ? 'PUT' : 'POST', values),
      editing?.id ? 'Expense updated' : 'Expense added'
    );

    if (ok) setEditing(null);
    return ok;
  }

  async function remove(item) {
    if (window.confirm(`Delete this ${money(item.amount)} expense?`)) {
      await mutate(() => send(`/expenses/${item.id}`, 'DELETE'), 'Expense deleted');
    }
  }

  return (
    <div className="page-body">
      <ExpenseHeader onAdd={() => setEditing({})} />
      <ExpenseFilters filters={filters} setFilters={setFilters} itemsCount={items.length} />

      {error ? (
        <ErrorState message={error} />
      ) : (
        <ExpenseTable items={items} onEdit={setEditing} onDelete={remove} />
      )}

      {editing && (
        <Modal title={editing.id ? 'Edit expense' : 'Add an expense'} onClose={() => setEditing(null)}>
          <ExpenseForm initial={editing} onSave={save} onCancel={() => setEditing(null)} busy={busy} />
        </Modal>
      )}
    </div>
  );
}
