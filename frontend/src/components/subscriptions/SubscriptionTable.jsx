import { dateLabel, EmptyState, money, pretty } from '../Common.jsx';

export default function SubscriptionTable({ items, onEdit, onCancel }) {
  return (
    <div className="table-wrap subscription-table">
      <table>
        <thead>
          <tr>
            <th>SERVICE</th>
            <th>CATEGORY</th>
            <th>BILLING</th>
            <th>NEXT RENEWAL</th>
            <th>STATUS</th>
            <th className="align-right">COST</th>
            <th aria-label="Actions" />
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.id}>
              <td>
                <div className="service-cell">
                  <span className="service-icon">{item.name.slice(0, 1).toUpperCase()}</span>
                  <strong>{item.name}</strong>
                </div>
              </td>
              <td>{item.category || 'Other'}</td>
              <td>{pretty(item.billingCycle)}</td>
              <td>{dateLabel(item.nextBillingDate)}</td>
              <td>
                <span className={`status-pill ${item.status === 'ACTIVE' ? 'status-active' : 'status-cancelled'}`}>
                  <i />{pretty(item.status)}
                </span>
              </td>
              <td className="amount-cell">{money(item.amount)}<small className="cost-cycle"> / {item.billingCycle === 'YEARLY' ? 'yr' : 'mo'}</small></td>
              <td>
                <div className="row-actions">
                  <button title="Edit subscription" onClick={() => onEdit(item)}>✎</button>
                  {item.status === 'ACTIVE' && <button title="Cancel subscription" onClick={() => onCancel(item)}>×</button>}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {!items.length && <EmptyState title="No subscriptions yet" copy="Add a recurring plan to track renewals and cost." />}
    </div>
  );
}
