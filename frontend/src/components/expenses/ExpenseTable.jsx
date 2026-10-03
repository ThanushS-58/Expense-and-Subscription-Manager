import { dateLabel, EmptyState, money, pretty } from '../Common.jsx';

export default function ExpenseTable({ items, onEdit, onDelete }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>DESCRIPTION</th>
            <th>CATEGORY</th>
            <th>DATE</th>
            <th>PAYMENT</th>
            <th className="align-right">AMOUNT</th>
            <th aria-label="Actions" />
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.id}>
              <td>
                <strong>{item.description || pretty(item.category)}</strong>
                <small className="mobile-date">{dateLabel(item.expenseDate)}</small>
              </td>
              <td><span className="category-pill">{pretty(item.category)}</span></td>
              <td>{dateLabel(item.expenseDate)}</td>
              <td>{pretty(item.paymentMethod)}</td>
              <td className="amount-cell">{money(item.amount)}</td>
              <td>
                <div className="row-actions">
                  <button title="Edit expense" onClick={() => onEdit(item)}>✎</button>
                  <button title="Delete expense" onClick={() => onDelete(item)}>×</button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {!items.length && <EmptyState title="No expenses found" copy="Add your first expense or adjust the filters." />}
    </div>
  );
}
