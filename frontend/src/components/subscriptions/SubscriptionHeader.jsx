export default function SubscriptionHeader({ onAdd }) {
  return (
    <div className="section-intro">
      <div>
        <span className="eyebrow">RECURRING COMMITMENTS</span>
        <h2>Subscriptions</h2>
        <p>Know what renews and what your recurring plans cost.</p>
      </div>
      <button className="button button-primary" onClick={onAdd}>＋ Add subscription</button>
    </div>
  );
}
