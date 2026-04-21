const todayEl = document.getElementById("today");
if (todayEl) {
  const now = new Date();
  todayEl.textContent = now.toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

const revealItems = document.querySelectorAll(".reveal");
revealItems.forEach((item, index) => {
  item.style.animationDelay = `${index * 80}ms`;
});
