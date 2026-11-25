import { euros } from '../api.js';

export default {
  name: 'ListeComptes',
  props: { comptes: Array },
  methods: { euros },
  template: `
    <section>
      <h2>Mes comptes</h2>
      <table>
        <thead>
          <tr><th>Compte</th><th>Type</th><th class="montant">Solde</th></tr>
        </thead>
        <tbody>
          <tr v-for="compte in comptes" :key="compte.numero">
            <td>{{ compte.numero }}</td>
            <td>{{ compte.type === 'EPARGNE' ? 'Épargne' : 'Courant' }}</td>
            <td class="montant">{{ euros(compte.solde) }}</td>
          </tr>
        </tbody>
      </table>
    </section>`
};
