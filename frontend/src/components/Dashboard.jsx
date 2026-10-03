import { useEffect, useState } from 'react';
import { request } from '../api.js';
import { money, dateLabel, EmptyState, ErrorState, Loading, pretty } from './Common.jsx';

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    request('/dashboard')
      .then(setData)
      .catch((err) => setError(err.message));
  }, []);

  if (error) return <ErrorState message={error} />;
  if (!data) return <Loading />;

  const maxCategory = Math.max(...(data.categorySummary || []).map((item) => item.total), 1);
  const currentMonth = new Date().toLocaleDateString('en-IN', { month: 'long', year: 'numeric' });

  return (
    <div className="page-body">
      <div className="welcome-line">
        <div>
          <span className="eyebrow">YOUR OVERVIEW</span>
          <h2>A little more clarity, every day.</h2>
        </div>
        <span className="month-chip">{currentMonth}</span>
      </div>

      <section className="metric-grid">
        <Metric label="Expenses this month" value={money(data.monthlyExpenseTotal)} icon="↗" tone="green" detail="Across all categories" />
        <Metric label="Active subscriptions" value={data.activeSubscriptionCount} icon="⟳" tone="blue" detail="Currently active" />
        <Metric label="Monthly subscription cost" value={money(data.monthlySubscriptionCost)} icon="◷" tone="coral" detail="Normalized monthly spend" />
        <Metric label="Yearly estimate" value={money(data.yearlySubscriptionCost)} icon="⌁" tone="yellow" detail="Based on active plans" />
      </section>

      <section className="dashboard-grid">
        <div className="panel category-panel">
          <div className="panel-heading">
            <div>
              <span className="eyebrow">WHERE IT GOES</span>
              <h3>Spending by category</h3>
            </div>
            <span className="panel-mark">↗</span>
          </div>

          {data.categorySummary?.length ? (
            <div className="category-list">
              {data.categorySummary.map((item, index) => (
                <div className="category-row" key={item.category}>
                  <div className="category-top">
                    <span><i className={`category-dot dot-${index % 5}`} />{pretty(item.category)}</span>
                    <strong>{money(item.total)}</strong>
                  </div>
                  <div className="bar-track">
                    <div className={`bar-fill fill-${index % 5}`} style={{ width: `${Math.max(3, (item.total / maxCategory) * 100)}%` }} />
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <EmptyState title="No spending recorded" copy="Add an expense to see your category breakdown." />
          )}
        </div>

        <div className="panel renewals-panel">
          <div className="panel-heading">
            <div>
              <span className="eyebrow">NEXT 7 DAYS</span>
              <h3>Coming up</h3>
            </div>
            <span className="renewal-count">{data.upcomingSubscriptions?.length || 0}</span>
          </div>

          {data.upcomingSubscriptions?.length ? (
            <div className="renewal-list">
              {data.upcomingSubscriptions.map((sub) => (
                <div className="renewal-row" key={sub.id}>
                  <div className="service-icon">{sub.name.slice(0, 1).toUpperCase()}</div>
                  <div className="renewal-info">
                    <strong>{sub.name}</strong>
                    <span>Renews {dateLabel(sub.nextBillingDate)}</span>
                  </div>
                  <b>{money(sub.amount)}</b>
                </div>
              ))}
            </div>
          ) : (
            <EmptyState title="Nothing due soon" copy="Your upcoming renewals will show here." />
          )}

          <div className="renewal-footer">
            <span>Active plan costs, all together.</span>
            <strong>{money(data.monthlySubscriptionCost)}<small> / month</small></strong>
          </div>
        </div>
      </section>

      <div className="insight-strip">
        <span className="insight-symbol">✳</span>
        <p><strong>Small insight</strong> Your yearly subscription estimate is {money(data.yearlySubscriptionCost)}. A quick review now can prevent surprise renewals later.</p>
      </div>
    </div>
  );
}

function Metric({ label, value, icon, tone, detail }) {
  return (
    <div className="metric-card">
      <div className="metric-head">
        <span>{label}</span>
        <i className={`metric-icon ${tone}`}>{icon}</i>
      </div>
      <strong className="metric-value">{value}</strong>
      <span className="metric-detail">{detail}</span>
    </div>
  );
}
