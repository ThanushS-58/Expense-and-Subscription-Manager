import { dateLabel, money } from '../Common.jsx';

export default function SubscriptionUpcoming({ items }) {
  return (
    <section className="upcoming-band">
      <div className="upcoming-band-heading">
        <span className="eyebrow">NEXT 7 DAYS</span>
        <h3>Upcoming renewals</h3>
      </div>

      {items.length ? (
        <div className="upcoming-band-items">
          {items.map((item) => (
            <div className="upcoming-chip" key={item.id}>
              <span className="service-icon">{item.name.slice(0, 1).toUpperCase()}</span>
              <span>
                <strong>{item.name}</strong>
                <small>{dateLabel(item.nextBillingDate)}</small>
              </span>
              <b>{money(item.amount)}</b>
            </div>
          ))}
        </div>
      ) : (
        <p className="upcoming-empty">No subscriptions renewing in the next 7 days.</p>
      )}
    </section>
  );
}
