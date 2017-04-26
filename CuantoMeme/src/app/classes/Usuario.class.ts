import { Vineta } from './Vineta.class';
import { Comentario } from './Comentario.class';

export class Usuario {
    
    public id: number;
    public avatarURL: string;
    public username: string;
    private password: string;
    public email: string;
    public publicaciones: Vineta[];
    public seguidos: Usuario[];
    public seguidores: Usuario[];
    //private comentarios: Comentario[];
    public likes: Vineta[];
    public dislikes: Vineta[];
    public favoritos: Vineta[];
    public subidas: Vineta[];
    public isLogged: Boolean = false;
    public roles: string[] = [];
    public isAdmin: boolean;


    constructor(id: number, username: string, avatar: string) {
        this.username = username;
        this.id = id;
        this.avatarURL = avatar;
    }
    getAvatar(): string {
        return this.avatarURL;
    }
    UserisAdmin(): boolean {
        return this.isAdmin;
    }
    getlikes(): Vineta[] {
        return this.likes;
    }
    getfavorites(): Vineta[] {
        return this.favoritos;
    }
    getdislikes(): Vineta[] {
        return this.dislikes;
    }
    setLogged(logged:Boolean){
        this.isLogged = logged;
    }
    setRoles(roles: string[]){
        this.roles = roles;  
        this.isAdmin = (this.roles.indexOf("ROLE_ADMIN") !== -1);
    }
    setSubidas(vinetas: Vineta[]){
        this.subidas = vinetas;
    }
    setFav(vinetas: Vineta[]){
        this.favoritos = vinetas;
    }
    setLikes(vinetas: Vineta[]){
        this.likes= vinetas;
    }
    setDislikes(vinetas: Vineta[]){
        this.dislikes= vinetas;
    }
    getRoles() {
        return this.roles;
    }
    getUsername() {
        return this.username;
    }
    getSubidas(): Vineta[] {
        return this.subidas;
    }
    addLike(viñeta:Vineta) {
        this.likes.push(viñeta);
    }
    addDislike(viñeta:Vineta) {
        this.dislikes.push(viñeta);
    }
    getEmail(): string {
        return this.email;
    }
}