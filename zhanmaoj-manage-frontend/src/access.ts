/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canUser: currentUser && currentUser.userRole === "user",
    canAdmin: currentUser && (currentUser.userRole === "admin" || currentUser.userRole === "super"),
    canSuper: currentUser && currentUser.userRole === "super",
  };
}
