// Appels a l'API REST de l'application (avec le jeton CSRF pour les requetes qui modifient des donnees)

function jetonCsrf() {
  const cookie = document.cookie.split('; ').find(c => c.startsWith('XSRF-TOKEN='));
  return cookie ? decodeURIComponent(cookie.split('=')[1]) : '';
}

export async function lire(url) {
  const reponse = await fetch(url);
  if (reponse.status === 401) {
    location.href = '/connexion.html';
  }
  return reponse.json();
}

export async function envoyer(url, corps) {
  const reponse = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'X-XSRF-TOKEN': jetonCsrf() },
    body: JSON.stringify(corps)
  });
  let contenu = {};
  try { contenu = await reponse.json(); } catch (e) { /* corps vide */ }
  return { ok: reponse.ok, statut: reponse.status, contenu };
}

export async function seDeconnecter() {
  await fetch('/logout', { method: 'POST', headers: { 'X-XSRF-TOKEN': jetonCsrf() } });
  location.href = '/connexion.html?deconnecte';
}

/** 1234.5 -> "1 234,50 €" (espaces ordinaires, pour des tests plus simples a ecrire) */
export function euros(montant) {
  const [entier, decimales] = Number(montant).toFixed(2).split('.');
  return entier.replace(/\B(?=(\d{3})+(?!\d))/g, ' ') + ',' + decimales + ' €';
}
