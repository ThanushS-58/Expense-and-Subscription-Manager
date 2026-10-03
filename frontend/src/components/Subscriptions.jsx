import { useEffect, useState } from 'react';
import { send } from '../api.js';
import { ErrorState, Modal, money } from './Common.jsx';
import SubscriptionForm from './subscriptions/SubscriptionForm.jsx';
import SubscriptionHeader from './subscriptions/SubscriptionHeader.jsx';
import SubscriptionTable from './subscriptions/SubscriptionTable.jsx';
import SubscriptionUpcoming from './subscriptions/SubscriptionUpcoming.jsx';

export default function Subscriptions({ mutate, busy }) {
  const [items, setItems] = useState([]);
  const [upcoming, setUpcoming] = useState([]);
  const [editing, setEditing] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([send('/subscriptions', 'GET'), send('/subscriptions/upcoming', 'GET')])
      .then(([all, soon]) => {
        setItems(all);
        setUpcoming(soon);
      })
      .catch((err) => setError(err.message));
  }, []);

  async function save(values) {
    const ok = await mutate(
      () => send(editing?.id ? `/subscriptions/${editing.id}` : '/subscriptions', editing?.id ? 'PUT' : 'POST', values),
      editing?.id ? 'Subscription updated' : 'Subscription added'
    );

    if (ok) setEditing(null);
    return ok;
  }

  async function cancel(item) {
    if (window.confirm(`Cancel ${item.name}? It will remain in your history.`)) {
      await mutate(() => send(`/subscriptions/${item.id}`, 'DELETE'), 'Subscription cancelled');
    }
  }

  return (
    <div className="page-body">
      <SubscriptionHeader onAdd={() => setEditing({})} />
      <SubscriptionUpcoming items={upcoming} />

      {error ? (
        <ErrorState message={error} />
      ) : (
        <SubscriptionTable items={items} onEdit={setEditing} onCancel={cancel} />
      )}

      {editing && (
        <Modal title={editing.id ? 'Edit subscription' : 'Add a subscription'} onClose={() => setEditing(null)}>
          <SubscriptionForm initial={editing} onSave={save} onCancel={() => setEditing(null)} busy={busy} />
        </Modal>
      )}
    </div>
  );
}
