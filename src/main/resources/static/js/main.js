// Gestion Ménagère Mali - JavaScript principal

document.addEventListener("DOMContentLoaded", function () {
  // Auto-dismiss alerts after 5 seconds with fade animation
  const alerts = document.querySelectorAll(".alert-dismissible");
  alerts.forEach(function (alert) {
    // Add progress bar for visual feedback
    const progressBar = document.createElement("div");
    progressBar.className = "alert-progress";
    progressBar.style.cssText =
      "position:absolute;bottom:0;left:0;height:3px;background:rgba(0,0,0,0.2);width:100%;animation:alertProgress 5s linear forwards;";
    alert.style.position = "relative";
    alert.style.overflow = "hidden";
    alert.appendChild(progressBar);

    setTimeout(function () {
      alert.classList.add("fade-out");
      setTimeout(function () {
        if (alert.parentNode) {
          const bsAlert = new bootstrap.Alert(alert);
          bsAlert.close();
        }
      }, 500);
    }, 5000);
  });

  // Add keyframe for progress bar
  if (!document.getElementById("alertProgressStyle")) {
    const style = document.createElement("style");
    style.id = "alertProgressStyle";
    style.textContent =
      "@keyframes alertProgress { from { width: 100%; } to { width: 0%; } }";
    document.head.appendChild(style);
  }

  // Form validation
  const forms = document.querySelectorAll(".needs-validation");
  forms.forEach(function (form) {
    form.addEventListener("submit", function (event) {
      if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      form.classList.add("was-validated");
    });
  });

  // Password confirmation validation
  const passwordFields = document.querySelectorAll(
    'input[name="confirmMotDePasse"]'
  );
  passwordFields.forEach(function (field) {
    field.addEventListener("input", function () {
      const form = field.closest("form");
      const password = form.querySelector('input[name="motDePasse"]');
      if (password && field.value !== password.value) {
        field.setCustomValidity("Les mots de passe ne correspondent pas");
      } else {
        field.setCustomValidity("");
      }
    });
  });

  // Tooltip initialization
  const tooltipTriggerList = document.querySelectorAll(
    '[data-bs-toggle="tooltip"]'
  );
  tooltipTriggerList.forEach(function (tooltipTriggerEl) {
    new bootstrap.Tooltip(tooltipTriggerEl);
  });

  // Popover initialization
  const popoverTriggerList = document.querySelectorAll(
    '[data-bs-toggle="popover"]'
  );
  popoverTriggerList.forEach(function (popoverTriggerEl) {
    new bootstrap.Popover(popoverTriggerEl);
  });

  // Confirm delete actions
  const deleteButtons = document.querySelectorAll("[data-confirm]");
  deleteButtons.forEach(function (button) {
    button.addEventListener("click", function (event) {
      const message =
        button.getAttribute("data-confirm") ||
        "Êtes-vous sûr de vouloir effectuer cette action ?";
      if (!confirm(message)) {
        event.preventDefault();
      }
    });
  });

  // Search form auto-submit on select change
  const searchSelects = document.querySelectorAll(".search-auto-submit");
  searchSelects.forEach(function (select) {
    select.addEventListener("change", function () {
      select.closest("form").submit();
    });
  });

  // Character counter for textareas
  const textareas = document.querySelectorAll("textarea[maxlength]");
  textareas.forEach(function (textarea) {
    const maxLength = textarea.getAttribute("maxlength");
    const counter = document.createElement("small");
    counter.className = "text-muted float-end";
    counter.textContent = `0/${maxLength}`;
    textarea.parentNode.appendChild(counter);

    textarea.addEventListener("input", function () {
      counter.textContent = `${textarea.value.length}/${maxLength}`;
    });
  });

  // Print functionality
  const printButtons = document.querySelectorAll("[data-print]");
  printButtons.forEach(function (button) {
    button.addEventListener("click", function () {
      window.print();
    });
  });

  // Back to top button
  const backToTop = document.getElementById("backToTop");
  if (backToTop) {
    window.addEventListener("scroll", function () {
      if (window.scrollY > 300) {
        backToTop.classList.add("show");
      } else {
        backToTop.classList.remove("show");
      }
    });

    backToTop.addEventListener("click", function () {
      window.scrollTo({ top: 0, behavior: "smooth" });
    });
  }
});

// Utility functions
function formatDate(dateString) {
  const options = { year: "numeric", month: "long", day: "numeric" };
  return new Date(dateString).toLocaleDateString("fr-FR", options);
}

function formatCurrency(amount) {
  return (
    new Intl.NumberFormat("fr-FR", {
      style: "decimal",
      minimumFractionDigits: 0,
    }).format(amount) + " FCFA"
  );
}
