export default function ExpenseHeader({ onAdd }) {
  return (
    <div className="section-intro">
      <div>
        <span className="eyebrow">YOUR DAY-TO-DAY SPENDING</span>
        <h2>Expenses</h2>
        <p>Keep track of what you spend, one entry at a time.</p>
      </div>
      <button className="button button-primary" onClick={onAdd}>＋ Add expense</button>
    </div>
  );
}
