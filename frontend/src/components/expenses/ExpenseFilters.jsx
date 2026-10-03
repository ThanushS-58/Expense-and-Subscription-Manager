import { expenseCategories } from '../Common.jsx';

export default function ExpenseFilters({ filters, setFilters, itemsCount }) {
  return (
    <div className="filter-row">
      <span className="filter-caption">FILTER BY</span>
      <select value={filters.category} onChange={(e) => setFilters({ ...filters, category: e.target.value })}>
        <option value="">All categories</option>
        {expenseCategories.map((value) => <option key={value}>{value}</option>)}
      </select>

      <label className="date-filter">From <input type="date" value={filters.fromDate} onChange={(e) => setFilters({ ...filters, fromDate: e.target.value })} /></label>
      <label className="date-filter">To <input type="date" value={filters.toDate} onChange={(e) => setFilters({ ...filters, toDate: e.target.value })} /></label>

      <button className="text-button" onClick={() => setFilters({ category: '', fromDate: '', toDate: '' })}>Clear</button>
      <span className="result-count">{itemsCount} {itemsCount === 1 ? 'expense' : 'expenses'}</span>
    </div>
  );
}
